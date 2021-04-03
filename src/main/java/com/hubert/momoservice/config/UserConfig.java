package com.hubert.momoservice.config;

import com.hubert.momoservice.entity.*;
import com.hubert.momoservice.repository.*;
import org.springframework.boot.CommandLineRunner;

import java.util.List;


public class UserConfig {

    public CommandLineRunner commandLineRunner(
            AppUserRepository appUserRepository,
            UserDetailRepository userDetailRepository,
            DeviceRepository deviceRepository,
            FingerprintRepository fingerprintRepository,
            LegalDocumentRepository legalDocumentRepository,
            MerchantRepository merchantRepository,
            PhoneNumberRepository phoneNumberRepository,
            TokenRepository tokenRepository,
            TransactionRepository transactionRepository,
            StatusRepository statusRepository,
            NetworkRepository networkRepository
    ) {
        /*
         *
         * Test Data for Database Design
         * [x] Devices
         * [x] fingerprints
         * [x] legal documents
         * [x] merchants
         * [x] Phone numbers
         * [x] Tokens
         * [x] Transactions
         * [x] Users
         * [x] Users' details
         *
         * */
        Role userRole = new Role(RoleType.USER);
        Role merchant = new Role(RoleType.MERCHANT);

        AppUser me = new AppUser("me@gmail.com", "123456");
        UserDetail meDetail = new UserDetail(
                "Francis",
                "Ampong",
                "",
                "plt 24 B",
                "Ashanti",
                "Kumasi",
                "Mamponteng"
        );

        meDetail.setUser(me);
        me.getRoles().addAll(List.of(userRole, merchant));

        AppUser lisa = new AppUser("lisa@gmail.com", "123456");
        UserDetail lisaDetail = new UserDetail(
                "Lisa",
                "Lisa",
                "",
                "plt 90 G",
                "Ashanti",
                "Kumasi",
                "Macro"
        );

        lisaDetail.setUser(lisa);
        lisa.getRoles().add(userRole);

        AppUser kwaku = new AppUser("kwaku@gmail.com", "123456");
        UserDetail kwakuDetail = new UserDetail(
                "Kwaku",
                "Hubert",
                "",
                "plt 80 f",
                "Ashanti",
                "Kumasi",
                "Santasi"
        );

        kwakuDetail.setUser(kwaku);
        kwaku.getRoles().addAll(List.of(userRole, merchant));

        Token token1 = new Token("dafdpifhaigfsdfia");
        token1.setUser(me);
        Token token2 = new Token("djafjdsaljfddafdlfd");
        token2.setUser(lisa);
        Token token3 = new Token("aduhuihashgbibeiapiap");
        token3.setUser(kwaku);

        Fingerprint fingerprint1 = new Fingerprint("https://www.hello.com");
        Fingerprint fingerprint2 = new Fingerprint("https://www.hello1.com");
        Fingerprint fingerprint3 = new Fingerprint("https://www.hello2.com");
        fingerprint1.setUser(me);
        fingerprint2.setUser(kwaku);
        fingerprint3.setUser(lisa);

        Network network = new Network(NetworkType.MTN);

        PhoneNumber phoneNumber1 = new PhoneNumber("0549754290", network);
        PhoneNumber phoneNumber2 = new PhoneNumber("0242555666", network);
        PhoneNumber phoneNumber3 = new PhoneNumber("0242559566", network);
        PhoneNumber phoneNumber4 = new PhoneNumber("0242559575", network);
        PhoneNumber phoneNumber5 = new PhoneNumber("0242009575", network);
        me.getPhoneNumbers().add(phoneNumber1);
        lisa.getPhoneNumbers().add(phoneNumber2);
        kwaku.getPhoneNumbers().add(phoneNumber3);

        Merchant merchant1 = new Merchant("Try Again", "yaw@yahoo.com", "Tafo plt 89 T", "Ashanti","Kumasi", me );
        Merchant merchant2 = new Merchant("Too Cheap", "tooCheap@gmail.com", "Asafo Street block 25", "Ashanti","Kumasi", lisa );
        merchant1.getPhoneNumbers().add(phoneNumber4);
        merchant2.getPhoneNumbers().add(phoneNumber5);

        LegalDocument legalDocument1 = new LegalDocument("https://www.legaldoc.com");
        legalDocument1.setMerchant(merchant1);
        LegalDocument legalDocument2 = new LegalDocument("https://www.legaldoc1.com");
        legalDocument2.setMerchant(merchant1);
        LegalDocument legalDocument3 = new LegalDocument("https://www.legaldoc3.com");
        legalDocument3.setMerchant(merchant2);

        Device device1 = new Device("test_device_1545", merchant1);
        Device device2 = new Device("test_device_1546", merchant2);
        Device device3 = new Device("test_device_1547", merchant2);

        Status status1 = new Status(StatusType.SUCCESS);
        Status status2 = new Status(StatusType.FAILED);
        Status status3 = new Status(StatusType.PENDING);

        Transaction transaction1 = new Transaction(phoneNumber1, phoneNumber1,500.0, status1);
        Transaction transaction2 = new Transaction(phoneNumber1, phoneNumber2,78.5, status3);
        Transaction transaction3 = new Transaction(phoneNumber2, phoneNumber5,52.0, status1);
        Transaction transaction4 = new Transaction(phoneNumber3, phoneNumber4,1000.0, status2);

        return args -> {
            statusRepository.saveAll(List.of(status1, status2, status3));
            networkRepository.save(network);
            phoneNumberRepository.saveAll(List.of(phoneNumber1, phoneNumber2, phoneNumber3, phoneNumber4, phoneNumber5));
            appUserRepository.saveAll(List.of(me, lisa, kwaku));
            userDetailRepository.saveAll(List.of(lisaDetail, meDetail, kwakuDetail));
            fingerprintRepository.saveAll(List.of(fingerprint1,fingerprint2, fingerprint3));
            merchantRepository.saveAll(List.of(merchant1, merchant2));
            legalDocumentRepository.saveAll(List.of(legalDocument1, legalDocument2, legalDocument3));
            deviceRepository.saveAll(List.of(device1, device2, device3));
            tokenRepository.saveAll(List.of(token1, token2, token3));
            transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));
        };
    }
}
