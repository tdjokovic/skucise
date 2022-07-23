import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OuragentsComponent } from './ouragents.component';

describe('OuragentsComponent', () => {
  let component: OuragentsComponent;
  let fixture: ComponentFixture<OuragentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OuragentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OuragentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
