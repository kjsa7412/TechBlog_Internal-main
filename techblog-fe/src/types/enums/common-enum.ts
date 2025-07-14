import exp from "node:constants";

export const enum EJWT {
    ExpiryTime = 60 * 60 * 1000,
    ExpiryTimeForTest = 1 * 60 * 1000,
    Expire_ERR = 'Expired TOKEN',
    Invalid_ERR = 'Invalid TOKEN',
    AccessToken = 'X-AUTH-TOKEN',
}

export const enum EIcon {
    Close = "Close",
    Close2 = "Close2",
    Close3 = "Close3",
    SignOut = "SignOut",
    ArrowUp = "ArrowUp",
    ArrowDown = "ArrowDown",
    TrashCan = "TrashCan",
    Picture = "Picture",
    Avatar = "Avatar",
    Option = "Option",
    Like1 = "Like1",
    Like2 = "Like2",
    Comment = "Comment",
    Search = 'Search'
}

export const enum EBlank {
    Row = 'Row',
    Column = 'Column',
    Header = 'Header'
}

export const enum EButtonSize {
    Small = 'Small',
    Medium = 'Medium',
    Large = 'Large'
}

export const enum EButtonShape {
    Round = 'Round',
    Square = 'Square'
}

export const enum EButtonType {
    Black = 'Black',
    Red = 'Red',
    Stroke = 'Stroke',
    None = 'None'
}

export const enum EElementId {
    HeaderProfile = 'HeaderProfile',
    BoardOption = 'BoardOption'
}

export const enum EInputShape {
    Round = 'Round',
    Square = 'Square'
}

export const enum EPopup {
    BoardOption = 'BaordOption',
    Confirm = 'Confirm',
    EditProfile = 'EditProfile',
    Notify = 'Notify',
    ProfileOption = 'ProfileOption',
    SignIn = 'SignIn',
    SignUp = 'SignUp',
    Search = 'Search'
}

export const enum EBannerType {
    Home = 'Home',
    Search = 'Search',
    Read = 'Read'
}

export const enum EBreakPoint {
    XS = 575,
    SM = 767,
    MD = 900,
    LG = 1248
}

export const enum EModalMutationStatus {
    Close = 'Close',
    Confirm = 'Confirm',
    Progress = 'Progress',
    Success = 'Success',
    Error = 'Error',
}