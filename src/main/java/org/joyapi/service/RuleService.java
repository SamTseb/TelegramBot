package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.model.Post;
import org.joyapi.service.eternal.RuleExternalService;
import org.jvnet.hk2.annotations.Service;

@AllArgsConstructor
@Service
public class RuleService {
    private final RuleExternalService sourceExternalService;

    public Post getPost(){
        return null;
    }
}
