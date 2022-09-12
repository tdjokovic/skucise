import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerInfoCardComponent } from './seller-info-card.component';

describe('SellerInfoCardComponent', () => {
  let component: SellerInfoCardComponent;
  let fixture: ComponentFixture<SellerInfoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SellerInfoCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SellerInfoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
