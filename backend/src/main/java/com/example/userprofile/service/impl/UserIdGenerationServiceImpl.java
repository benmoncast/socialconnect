package com.example.userprofile.service.impl;

import com.example.userprofile.exception.DailyUserIdLimitExceededException;
import com.example.userprofile.repository.UserProfileIdSequenceRepository;
import com.example.userprofile.repository.UserProfileRepository;
import com.example.userprofile.service.UserIdGenerationService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserIdGenerationServiceImpl implements UserIdGenerationService {

    private static final DateTimeFormatter DATE_KEY_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    private static final int MAX_DAILY_SERIES = 9999;

    private final UserProfileRepository userProfileRepository;
    private final UserProfileIdSequenceRepository userProfileIdSequenceRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Long generateUserId() {
        String dateKey = LocalDate.now().format(DATE_KEY_FORMATTER);
        int initialSeries = findLatestSeriesForDate(dateKey) + 1;

        userProfileIdSequenceRepository.reserveNextSeries(dateKey, initialSeries);
        int reservedSeries = userProfileIdSequenceRepository.getReservedSeries().intValue();

        if (reservedSeries > MAX_DAILY_SERIES) {
            throw new DailyUserIdLimitExceededException(
                    "Daily user ID series limit reached for " + dateKey + ". Maximum series is 9999."
            );
        }

        String firstTenDigits = dateKey + String.format("%04d", reservedSeries);
        int checkDigit = computeCheckDigit(firstTenDigits);

        return Long.valueOf(firstTenDigits + checkDigit);
    }

    private int findLatestSeriesForDate(String dateKey) {
        Long lowerBound = Long.valueOf(dateKey + "00000");
        Long upperBound = Long.valueOf(dateKey + "99999");

        return userProfileRepository.findLatestGeneratedUserIdForDate(lowerBound, upperBound)
                .map(this::extractSeriesNumber)
                .orElse(0);
    }

    private int extractSeriesNumber(Long generatedUserId) {
        String normalizedUserId = String.format("%011d", generatedUserId);
        return Integer.parseInt(normalizedUserId.substring(6, 10));
    }

    private int computeCheckDigit(String firstTenDigits) {
        int sum = 0;

        for (int index = 0; index < firstTenDigits.length(); index++) {
            int digit = Character.getNumericValue(firstTenDigits.charAt(index));

            /*
             * Check digit rule:
             * multiply every other digit by 2, starting with the first digit.
             * If the product has two digits, add those digits together
             * before adding it to the total sum.
             */
            if (index % 2 == 0) {
                int product = digit * 2;
                sum += product > 9 ? (product / 10) + (product % 10) : product;
            } else {
                sum += digit;
            }
        }

        int nextMultipleOfTen = ((sum + 9) / 10) * 10;
        return nextMultipleOfTen - sum;
    }
}
