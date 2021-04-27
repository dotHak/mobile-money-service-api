package com.hubert.momoservice;

import com.hubert.momoservice.controller.FingerprintController;
import com.hubert.momoservice.entity.Fingerprint;
import com.hubert.momoservice.repository.FingerprintRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FingerprintControllerTest {

    @Mock
    private FingerprintRepository repository;

    @Mock
    private FingerprintController fingerprintController;

    @Test
    public void testGetFingerprint(){
        Fingerprint fingerprint = fingerprintController.getOne(1L);

        Fingerprint fingerprint1 = repository.getOne(1L);

        assert fingerprint == fingerprint1;
    }
}
