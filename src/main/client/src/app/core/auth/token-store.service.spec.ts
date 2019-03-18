import { TestBed } from '@angular/core/testing';

import { TokenStoreService } from './token-store.service';

describe('TokenStoreService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TokenStoreService = TestBed.get(TokenStoreService);
    expect(service).toBeTruthy();
  });
});
