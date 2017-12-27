package com.bizdata.app.maguser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.maguser.domain.MagUser;

import me.sdevil507.base.JpaBaseRepository;

import java.util.List;

public interface MagUserRepository extends JpaBaseRepository<MagUser, String> {
    // 根据accountnum查找
    MagUser findByAccountnum(int accountnum);

    // 根据accountnum获取主键值id
    @Query(value = "select id from usr_account where accountnum = ?1", nativeQuery = true)
    String getIdByAccountnum(int accountnum);

    @Query(value = "update usr_account set miao_money = miao_money + ?2 where accountnum = ?1", nativeQuery = true)
    @Modifying
    void updateMiao(int accountnum, int num);

    @Query(value = "select accountnum from usr_account where id = ?1", nativeQuery = true)
    Integer getAccountnumById(String id);

    @Query(value = "select mobile from MagUser as a where a.id = ?1")
    String getMobileById(String id);

    @Query(value = "select a from MagUser as a, UserProfile as b where a.id = b.accountId and b.nickName = ?1")
    Page<MagUser> findAll(Pageable pageable, String userName);

    @Query(value = "select a.mobile, b.nickName from MagUser as a, UserProfile as b where a.id = b.accountId")
    Page<MagUser> findByCity(MagUser magUser, Pageable pageable);

//	@Query(value = "select com.bizdata.app.maguser.domain.Magnar(a.mobile as mobile, b.nickName as nickName) from MagUser as a, UserProfile as b where a.id = b.accountId")
//	Page<Magnar> findByCity(Pageable pageable);

    @Query(value = "select b.accountId from UserProfile as b where b.accountId = ?1")
    Page<MagUser> findAll(Pageable pageable, MagUser magUser);

    /**
     * 根据手机号查询符合条件的userId，理论上讲应该最多一条
     * @param mobile
     * @return
     */
    @Query(value = "SELECT a.id from MagUser as a where a.mobile = ?1")
    List<String> findIdByMobile(String mobile);
}
