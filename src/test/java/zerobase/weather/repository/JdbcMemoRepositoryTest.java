package zerobase.weather.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    @DisplayName("테이블 컬럼 추가")
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(1, "Hello world");

        //when
        jdbcMemoRepository.save(newMemo);

        //then
        Optional<Memo> optionalMemo = jdbcMemoRepository.findById(1);
        assertEquals(optionalMemo.get().getText(), "Hello world");
    }

    @Test
    @DisplayName("테이블 컬럼 찾기")
    void findAllMemoTest() {
        //given
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println("memoList = " + memoList);

        //when
        //then
        assertNotNull(memoList);
    }
}