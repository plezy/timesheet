export class ApplicationSetting {
    id: number;
    settingId: string;
    dateLinked: boolean;
    // dates value that was requested or current date.
    applicableDate: Date;
    // Note: in DB the recorded date is end of applicability is INCLUSIVE !
    // gives end of applicability for the returned setting
    endOfApplicabilityDate: Date;
    value: string;
}

export class ApplicationSettingRaw {
    id: number;
    settingId: string;
    dateLinked: boolean;
    sorting: number;
    javaType: string;
    value: string;
    datedValues: ApplicationSettingDatedValueRaw[];
}

export class ApplicationSettingDatedValueRaw {
    id: number;
    value: string;
    dateEndValid: Date;
}
