package com.tracer.utils;

import com.tracer.util.KeyGeneratorUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class KeyGeneratorUtilityTest {
    @Mock
    KeyPairGenerator keyPairGenerator;
    @InjectMocks
    private KeyGeneratorUtility keyGeneratorUtility;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void generateRsaKey_KeyPair() {
        // GIVEN

        // WHEN
        KeyPair response = KeyGeneratorUtility.generateRsaKey();

        // THEN
        assertNotNull(response);
    }
}
