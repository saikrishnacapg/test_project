package com.sanju.bm.core.service;

import com.sanju.bm.config.Translator;
import com.sanju.bm.core.entity.PublisherEntity;
import com.sanju.bm.core.enums.Status;
import com.sanju.bm.core.params.PublisherParam;
import com.sanju.bm.core.repository.PublisherRepository;
import com.sanju.bm.core.util.ParamAndEntityBuilder;
import com.sanju.bm.errorhandler.ErrorCodes;
import com.sanju.bm.errorhandler.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLEngineResult;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private static final Logger log = LoggerFactory.getLogger(PublisherServiceImpl.class);

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    ParamAndEntityBuilder paramAndEntityBuilder;

    @Autowired
    Translator translator;

    @Override
    public void addPublisher(PublisherParam param) {
        PublisherEntity entity = paramAndEntityBuilder.publisherParamToEntity(param);
        entity.setStatus(Status.ACTIVE);
        publisherRepository.save(entity);
    }

    @Override
    public List<PublisherParam> getAll() throws ResourceNotFoundException {

        List<PublisherEntity> publisherEntities = publisherRepository.findAll();
        if(publisherEntities.isEmpty()){
            throw new ResourceNotFoundException(ErrorCodes.Feature.PUBLIHER_GET,
                    ErrorCodes.CODE.PUBLISHER_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.PUBLISHER_NOT_FOUND)));
        }
        return publisherEntities.stream()
                .filter(x->(x.getStatus() != null && x.getStatus().equals(Status.ACTIVE)))
                .map(x-> paramAndEntityBuilder.publisherEntityToParam(x))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePublisher(PublisherParam requestToParam) {
        requestToParam.setStatus(Status.ACTIVE);
        publisherRepository.saveAndFlush(paramAndEntityBuilder.publisherParamToEntity(requestToParam));
    }

    @Override
    public PublisherParam getOne(long id) throws ResourceNotFoundException {
        PublisherEntity entity = publisherRepository.getOne(id);
        if(entity == null){
            throw new ResourceNotFoundException(ErrorCodes.Feature.PUBLIHER_GET,
                    ErrorCodes.CODE.PUBLISHER_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.PUBLISHER_NOT_FOUND)));
        }
        return paramAndEntityBuilder.publisherEntityToParam(entity);
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException {
        PublisherEntity entity = publisherRepository.getOne(id);
        if(entity == null){
            throw new ResourceNotFoundException(ErrorCodes.Feature.PUBLIHER_UPDATE,
                    ErrorCodes.CODE.PUBLISHER_NOT_FOUND, translator.toLocale(ErrorCodes.REASON_MAP.get(ErrorCodes.CODE.PUBLISHER_NOT_FOUND)));
        }
        entity.setStatus(Status.DELETED);
        publisherRepository.saveAndFlush(entity);

    }
}
