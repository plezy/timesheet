export class ApplicationSetting {
    id: number;
    settingId: string;
    dateLinked: boolean;
    // dates value was requested or date value is applicable.
    // Note: in DB the recorded date is end of applicability INCLUSIVE ! SO, end of applicability = applicableDate - 1 day 
    applicableDate: Date;
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