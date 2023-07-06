package subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;


@TestConstructor(autowireMode = AutowireMode.ALL) // 생성자 주입 가능하도록! final 좋아~
@DataJpaTest // SliceTest 가즈아, 사용된 순간 JPA 관련된 것들이 모두 부트한테로 업 된다고 생각하셈
// 기본적으로 @Transactional 어노테이션 붙어있어서 먼저 트랜잭션 시작하고 롤백하는 과정들을 거침
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationRepositoryTest {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    StationRepositoryTest(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Test
    void save_테스트() {
        // given
        final Station expected = new Station("잠실역");

        // when
        final Station actual = stationRepository.save(expected);

        // then
        Assertions.assertAll(
                () ->assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("잠실역")
        );
    }

    @Test
    void findByName_테스트() {
        // given
        stationRepository.save(new Station("선릉역"));

        // when
        final Station actual = stationRepository.findByName("선릉역");

        // then
        assertThat(actual.getName()).isEqualTo("선릉역");
    }

    @Test
    void 동일성_테스트_1() {
        // given
        final Station before = stationRepository.save(new Station("잠실역"));

        // when
        final Station after = stationRepository.findById(before.getId()).get();

        // 1차 캐시 갔더니 있어서 굳이 쿼리 안써도 된다~
        // then
        assertThat(before == after).isTrue();
    }

    // 내부를 가보면 영속성컨텍스트는 concurrentHashMap으로 되어있음
    // 인메모리 데이터베이스라고 생각해도 됨
    // id : key, value : instance

    @Test
    void 동일성_테스트_2() {
        // given
        final Station before = stationRepository.save(new Station("잠실역"));

        // when
        final Station after = stationRepository.findByName("잠실역");
        // 1차 캐시에 없으면 데이터베이스에 갔다오고
        // 데이터베이스 갔다 와서 1차캐시 저장 후
        // 우리에게 보여줌

        // then
        assertThat(before == after).isTrue();
    }

    @Test
    void 변경_감지() {
        // given
        final Station before = stationRepository.save(new Station("잠실역"));
        before.updatName("선릉역");

        // when
        final Station after = stationRepository.findByName("선릉역");

        // then
        assertThat(after).isNotNull();
    }

    @Test
    void Line과_함께_저장한다() {
        // given
        final Station station = new Station("선릉역");
        final Line line = lineRepository.save(new Line("2호선"));
        station.setLine(line);
        // line에 대한 정보를 미리 영속화 시켜야 함

        // when
        stationRepository.save(station);

        // then
        stationRepository.flush();
    }

    @Test
    void test_findByNameWithLine() {
        // given
        final Station station = stationRepository.findByName("교대역");

        // expect
        assertThat(station).isNotNull();
        assertThat(station.getLine()).isNotNull();
    }

    @Test
    void test_updateWithLine() {
        // given
        final Station station = stationRepository.findByName("교대역");

        // when
        station.setLine(lineRepository.save(new Line("3호선")));
        // 항상 영속화 된 놈을 넣어줘야 함

        // then
        stationRepository.flush();
    }

    @Test
    void test_removeLineInStation() {
        // given
        final Station station = stationRepository.findByName("교대역");

        // when
        station.setLine(null);
        // 객체 중심으로 사고를 하세용..

        // then
        stationRepository.flush();
    }

    @Test
    void test_removeLine() {
        // given
        final Line line = lineRepository.findByName("3호선");

        // when
        lineRepository.delete(line);

        // then
        lineRepository.flush(); // -> 에러, 해당 라인을 사용하고 있는 station이 있음
        // 연관관계를 지워야 삭제할 수 있음
    }


}
