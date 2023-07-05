package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
class QuestionRepositoryTest {

    private final QuestionRepository questionRepository;

    QuestionRepositoryTest(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Test
    void 삭제되지_않은_질문들을_찾을_수_있다() {
        // given
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        // when
        final List<Question> result = questionRepository.findByDeletedFalse();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void id로_질문을_찾을_수_있다() {
        // given
        final Question question = questionRepository.save(QuestionTest.Q2);

        // when
        final Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).isEqualTo(question)
        );
    }
}