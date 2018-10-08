package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.SysInformationDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yd
 */
public interface SysInformationRepository extends JpaRepository<SysInformationDO, Long> {

    /**
     * 通过 id，delete 查找
     *
     * @param id
     * @param delete
     * @return
     */
    SysInformationDO findFirstByIdAndDelete(Long id, Boolean delete);

    /**
     * 通过 name，delete 查找
     *
     * @param name
     * @param delete
     * @return
     */
    SysInformationDO findFirstByNameAndDelete(String name, Boolean delete);

}
