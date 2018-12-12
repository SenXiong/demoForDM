package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonRepository extends JpaRepository<PersonExtend, Long> {

    @Query(value="select extractvalue(psn, '/research/subject_name') from (select t.column_value as psn from xmltable('/data/researches/research' passing (select psn_xml from person_extend where psn_code=?1)) t) t2",nativeQuery = true)
    List<String> getxmldemo(Long psnCode);

    @Modifying
    @Query(value = "insert into t_sys_org_user(org_id,user_id) values(?1,?2)",nativeQuery = true)
    int addUserToOrg(Long orgId,Long userId);
}