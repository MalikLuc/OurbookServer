package com.example.v2ourbook.services;

import com.example.v2ourbook.models.BookClub;
import com.example.v2ourbook.models.Licence;
import com.example.v2ourbook.dto.LicenceDto;
import com.example.v2ourbook.repositories.BookClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenceService {

    BookClubRepository bookClubRepository;

    public LicenceService(BookClubRepository bookClubRepository) {
        this.bookClubRepository = bookClubRepository;
    }

    public Licence update(Licence licence, LicenceDto licenceDto){
        licence.setKeepLicence(licenceDto.isKeep());
        licence.setReturnToGiver(licenceDto.isReturnToGiver());
        licence.setWeeksUsageTime(licenceDto.getWeeksUsageTime());
        List<BookClub> bookClubs = licenceDto.getBookClubs().stream().
                map(b -> bookClubRepository.findBookClubById(b)).collect(Collectors.toList());
        licence.setBookClubs(bookClubs);
        return licence;
    }
}
