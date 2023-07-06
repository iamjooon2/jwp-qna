package subway;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@TestConstructor(autowireMode = AutowireMode.ALL)
@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineRepositoryTest {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    LineRepositoryTest(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Test
    void test_findByName() {
        // given
        final Line line = lineRepository.findByName("3호선");

        // when
        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    void save() {
        // given
        final Line line = new Line("3호선");

        // when
        line.addStation(new Station("선릉역"));
        lineRepository.save(line);

        // then
        lineRepository.flush();
    }
}
