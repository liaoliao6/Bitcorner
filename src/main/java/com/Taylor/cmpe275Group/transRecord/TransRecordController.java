package com.Taylor.cmpe275Group.transRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.Taylor.cmpe275Group.AppUser.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransRecordController {

    @Autowired
    AppUserRepository appUserRepository;

    @GetMapping(path = "/transOfUser/{email}/{sDateTime}/{eDateTime}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<TransRecord> getTransRecordsOfAppUser(
            @PathVariable(value = "email") String email,
            @PathVariable(value = "sDateTime") String sDateTime,
            @PathVariable(value = "eDateTime") String eDateTime) {
        System.out.println("In orderRecord Controller");

        AppUser existUser = appUserRepository.findByEmail(email);
        if(existUser == null) {
            return new ArrayList<>();
        }
        List<TransRecord> transRecords = existUser.getTransRecords();
        if(sDateTime == null || eDateTime == null || sDateTime.equals("null") || sDateTime.equals("null")){
            return transRecords;
        }

        sDateTime += "T00:00:00";
        eDateTime += "T23:59:59";
        LocalDateTime startDateTime = LocalDateTime.parse(sDateTime);
        LocalDateTime endDateTime = LocalDateTime.parse(eDateTime);

        List<TransRecord> userTransRecords = new ArrayList<>();
        for(TransRecord tr : transRecords){
            if((tr.getSeller().equals(email)||tr.getBuyer().equals(email))
                    &&((tr.getEndDateTime().isEqual(startDateTime))
                    ||(tr.getEndDateTime().isAfter(startDateTime))
                    ||(tr.getEndDateTime().isEqual(endDateTime))
                    ||(tr.getEndDateTime().isBefore(endDateTime))))
                userTransRecords.add(tr);
        }
        return userTransRecords;
    }
}
