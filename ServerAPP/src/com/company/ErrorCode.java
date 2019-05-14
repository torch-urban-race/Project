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
    WrongUserID,
    WrongTorchID
}
