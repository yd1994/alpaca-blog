package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.FileDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDO, Long> {

    /**
     * 通过 id，delete 获取
     * @param id
     * @param delete
     * @return
     */
    FileDO findFirstByIdAndDelete(Long id, Boolean delete);

    /**
     * 通过 id， type, delete 获取
     * @param id
     * @param type
     * @param delete
     * @return
     */
    FileDO findFirstByIdAndTypeAndDelete(Long id, Integer type, Boolean delete);

}
