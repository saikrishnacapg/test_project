package com.sanju.bm.core.service;

import com.sanju.bm.config.Translator;
import com.sanju.bm.core.entity.AuthorEntity;
import com.sanju.bm.core.enums.Status;
import com.sanju.bm.core.params.AuthorParam;
import com.sanju.bm.core.repository.AuthorRepository;
import com.sanju.bm.core.util.ParamAndEntityBuilder;
import com.sanju.bm.errorhandler.ErrorCodes;
import com.sanju.bm.errorhandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ParamAndEntityBuilder paramAndEntityBuilder;

    @Autowired
    private Translator translator;

    @Override
    public void addAuthor(AuthorParam param) {
        AuthorEntity authorEntity = paramAndEntityBuilder.authorParamToEntity(param);
        authorEntity.setCreated(new Date());
        authorEntity.setStatus(Status.ACTIVE);
        authorRepository.saveAndFlush(authorEntity);
    }

    @Override
    public List<AuthorParam> getAll() throws ResourceNotFoundException {
        List<AuthorParam> paramList;
        List<AuthorEntity> entities = authorRepository.findAll();
        if(entities.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCodes.Feature.AUTHOR_GET,
                    ErrorCodes.CODE.AUTHOR_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.AUTHOR_NOT_FOUND)));
        }

        paramList = entities.stream()
                .filter(x -> x.getStatus().equals(Status.ACTIVE))
                .map(x -> paramAndEntityBuilder.authorEntityToParam(x))
                .collect(Collectors.toList());
        return paramList;
    }

    @Override
    public void updateAuthor(AuthorParam requestToParam) {
        requestToParam.setStatus(Status.ACTIVE);
        authorRepository.saveAndFlush(paramAndEntityBuilder.authorParamToEntity(requestToParam));
    }

    @Override
    public AuthorParam getOne(long id) throws ResourceNotFoundException {
        AuthorEntity authorEntity = authorRepository.getOne(id);
        if(authorEntity == null || !authorEntity.getStatus().name().equals(Status.ACTIVE.name())){
            throw new ResourceNotFoundException(ErrorCodes.Feature.AUTHOR_GET,
                    ErrorCodes.CODE.AUTHOR_NOT_FOUND,translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.AUTHOR_NOT_FOUND)));
        }
        return paramAndEntityBuilder.authorEntityToParam(authorEntity);
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException {
        AuthorEntity authorEntity = authorRepository.getOne(id);
        if(authorEntity != null) {
            authorEntity.setStatus(Status.DELETED);
            authorRepository.save(authorEntity);
        }
        else {
            throw new ResourceNotFoundException(ErrorCodes.Feature.AUTHOR_GET,
                    ErrorCodes.CODE.AUTHOR_NOT_FOUND,translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.AUTHOR_NOT_FOUND)));
        }
    }
}
