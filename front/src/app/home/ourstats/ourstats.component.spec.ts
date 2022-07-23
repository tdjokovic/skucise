import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OurstatsComponent } from './ourstats.component';

describe('OurstatsComponent', () => {
  let component: OurstatsComponent;
  let fixture: ComponentFixture<OurstatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OurstatsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OurstatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
