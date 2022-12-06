package com.juliy.ims.dao;

import com.juliy.ims.entity.Record;

/**
 * record表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:56
 */
public interface RecordDao {
    /**
     * 添加新记录
     * @param rec 记录
     * @return 添加成功返回true，添加失败返回false
     */
    boolean insert(Record rec);
}
