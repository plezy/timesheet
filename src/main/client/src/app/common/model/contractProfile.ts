import { User } from './user';

export class ContractProfile {
    id: number;
    name: string;
    description: string;
    completed: boolean;
    hourlyRate: number;
    maximumDailyInvoiced: number;
    minimumDailyInvoiced: number;
    multipleUnitInvoiced: number;
    assignees: User[];
}
