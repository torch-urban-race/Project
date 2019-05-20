package com.company;

public enum ErrorCode {
    InvalidCommand,
    OK,
    NameTooShortOrLong,
    PasswordTooLongOrShort,
    SQLError,
    NameContainsIllegalSymbols,
    PasswordContainsIllegalSymbols,
    NameAlreadyExists,
    NameDoesNotExist,
    PasswordDoesNotMatch,
    AchievementDoesNotExist,
    WrongUserID,
    WrongTorchID,
    WrongAchievementID
}
