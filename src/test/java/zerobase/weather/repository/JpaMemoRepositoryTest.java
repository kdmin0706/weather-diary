package zerobase.weather.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {
    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void findAllMemoTest() {
        //given
        List<Memo> memoList = jpaMemoRepository.findAll();
        //when
        //then
        assertNotNull(memoList);
    }

    @Test
    @DisplayName("DB 테이블 데이터 삽입")
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(10, "this is jpa");
        //when
        jpaMemoRepository.save(newMemo);
        List<Memo> memoList = jpaMemoRepository.findAll();

        //then
        assertTrue(!memoList.isEmpty());
    }

    @Test
    @DisplayName("DB 테이블 데이터 확인")
    void findByIdTest() {
        //given
        Memo newMemo = new Memo(11, "jpa");

        //when
        Memo savedMemo = jpaMemoRepository.save(newMemo);
        Optional<Memo> optionalMemo = jpaMemoRepository.findById(savedMemo.getId());

        //then
        assertEquals("jpa", optionalMemo.get().getText());
    }

}