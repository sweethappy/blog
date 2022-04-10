package com.yjh.blob.Service;

import com.yjh.blob.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    Type getTypeName(String name);
    List<Type> listType();
    List<Type> listTypeTop(Integer size);
}
