import { User } from './user';

export class ContractProfile {
    id: number;
    name: string;
    description: string;
    completed: false;
    hourlyRate: number;
    maximumDailyInvoiced: number;
    minimumDailyInvoiced: number;
    multipleUnitInvoiced: number;
    assignees: User[];
}
