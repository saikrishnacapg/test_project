package com.sanju.bm.core.service;

import com.sanju.bm.config.Translator;
import com.sanju.bm.core.entity.TagEntity;
import com.sanju.bm.core.enums.Status;
import com.sanju.bm.core.params.TagParam;
import com.sanju.bm.core.repository.TagRepository;
import com.sanju.bm.core.util.ParamAndEntityBuilder;
import com.sanju.bm.errorhandler.ErrorCodes;
import com.sanju.bm.errorhandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ParamAndEntityBuilder paramAndEntityBuilder;

    @Autowired
    private Translator translator;

    @Override
    public void add(TagParam param) {
        param.setStatus(Status.ACTIVE);
        tagRepository.save(paramAndEntityBuilder.tagParamToEntity(param));
    }

    @Override
    public void update(TagParam param) {
        param.setStatus(Status.ACTIVE);
        tagRepository.save(paramAndEntityBuilder.tagParamToEntity(param));
    }

    @Override
    public TagParam getTag(long id) throws ResourceNotFoundException {
        TagEntity tagEntity = tagRepository.getOne(id);
        if (tagEntity == null) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.TAG_GET,
                    ErrorCodes.CODE.TAG_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.TAG_NOT_FOUND)));
        }

        return paramAndEntityBuilder.tagEntityToParam(tagEntity);
    }

    @Override
    public List<TagParam> getAll() throws ResourceNotFoundException {
        List<TagEntity> tagEntities = tagRepository.findAll();
        if (tagEntities.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.TAG_GET,
                    ErrorCodes.CODE.TAG_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.TAG_NOT_FOUND)));
        }

        return tagEntities.stream()
                .filter(x -> x.getStatus().equals(Status.ACTIVE))
                .map(x -> paramAndEntityBuilder.tagEntityToParam(x))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException {
        TagEntity tagEntity = tagRepository.getOne(id);
        if (tagEntity == null) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.TAG_DELETE,
                    ErrorCodes.CODE.TAG_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.TAG_NOT_FOUND)));
        }

        tagEntity.setStatus(Status.DELETED);
        tagRepository.save(tagEntity);
    }
}
