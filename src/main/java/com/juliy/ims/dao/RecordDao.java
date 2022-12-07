package com.juliy.ims.dao;

import com.juliy.ims.entity.Record;
import com.juliy.ims.entity.table_unit.RecordDO;

import java.util.List;

/**
 * record表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:56
 */
public interface RecordDao {

    /**
     * 查询所有记录信息
     * @return 记录信息列表
     */
    List<RecordDO> queryAll();

    /**
     * 查询指定记录信息
     * @param sql 查询语句
     * @return 符合要求的记录信息列表；若不存在则列表长度为0
     */
    List<RecordDO> query(String sql);

    /**
     * 查询指定记录信息的条数
     * @param sql 查询语句
     * @return 满足条件的记录信息总数
     */
    int queryCount(String sql);

    /**
     * 添加新记录
     * @param rec 记录
     */
    void insert(Record rec);
}
