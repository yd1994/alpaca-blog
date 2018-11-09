package com.yd1994.alpacablog.common.base;

import com.yd1994.alpacablog.common.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 基础 Controller
 *
 * @param <T>
 * @param <E>
 * @author yd
 */
public abstract class BaseRestController<T, E extends BaseService<T>> {

    @Autowired
    private E e;

    @GetMapping("/{id}")
    public ResultFactory.Info get(@PathVariable Long id) {
        return ResultFactory.get200Info().data(this.e.get(id));
    }

    @PostMapping
    public ResultFactory.Info add(T t) {
        this.e.add(t);
        return ResultFactory.get200Info().message("添加成功");
    }

    @PutMapping("/{id}")
    public ResultFactory.Info update(T t, @PathVariable Long id) {
        this.e.update(t, id);
        return ResultFactory.get200Info().message("修改成功");
    }

    @DeleteMapping("/{id}")
    public ResultFactory.Info delete(@PathVariable Long id) {
        this.e.delete(id);
        return ResultFactory.get200Info().message("删除成功");
    }

}
