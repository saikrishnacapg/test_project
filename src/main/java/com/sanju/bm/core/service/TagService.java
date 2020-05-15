package com.sanju.bm.core.service;

import com.sanju.bm.core.params.TagParam;
import com.sanju.bm.errorhandler.ResourceNotFoundException;

import java.util.List;

public interface TagService {
    void add(TagParam param);
    void update(TagParam param);
    TagParam getTag(long id) throws ResourceNotFoundException;
    List<TagParam> getAll() throws ResourceNotFoundException;
    void delete(long id) throws ResourceNotFoundException;
}
