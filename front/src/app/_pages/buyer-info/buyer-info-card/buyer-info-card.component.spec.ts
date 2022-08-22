import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyerInfoCardComponent } from './buyer-info-card.component';

describe('BuyerInfoCardComponent', () => {
  let component: BuyerInfoCardComponent;
  let fixture: ComponentFixture<BuyerInfoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuyerInfoCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BuyerInfoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
