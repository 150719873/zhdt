package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <F>
 * @param <V>
 */
public interface IWaterObtainFactory<F extends BaseForm, V extends BaseVo> {

    /**
     * 获取水表
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V getDevice(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 新增设备
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V addDevice(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 编辑设备
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V updateDevice(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取水表列表
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default List<V> listDevice(F f) throws FrameworkRuntimeException {
        return new ArrayList<>();
    }

    /**
     * 获取水表列表并分页
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default Pagination<V> pageDevice(F f) throws FrameworkRuntimeException {
        return new Pagination<>();
    }

    /**
     * 获取水表数据
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V getDeviceUplink(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取水表数据列表
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default List<V> listDeviceUplink(F f) throws FrameworkRuntimeException {
        return new ArrayList<>();
    }

    /**
     * 获取水表数据列表并分页
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default Pagination<V> pageDeviceUplink(F f) throws FrameworkRuntimeException {
        return new Pagination<>();
    }

    /**
     * 获取客户资料详情
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V getOwner(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 新增客户资料详情
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V addOwner(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 编辑客户资料详情
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V updateOwner(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取客户资料列表
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default List<V> listOwner(F f) throws FrameworkRuntimeException {
        return new ArrayList<>();
    }

    /**
     * 获取客户资料列表并分页
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default Pagination<V> pageOwner(F f) throws FrameworkRuntimeException {
        return new Pagination<>();
    }

    /**
     * 取消命令
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V cancelCommand(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 发送命令
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V sendCommand(F f) throws FrameworkRuntimeException {
        return null;
    }

    /**
     * 获取命令状态
     * @param f
     * @return
     * @throws FrameworkRuntimeException
     */
    default V getCommand(F f) throws FrameworkRuntimeException {
        return null;
    }
}
