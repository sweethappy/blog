package com.yjh.blob.Service;

import com.yjh.blob.po.Tag;
import com.yjh.blob.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    void deleteType(Long id);

    Tag updataType(Long id, Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    Tag getTagName(String name);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);
}
