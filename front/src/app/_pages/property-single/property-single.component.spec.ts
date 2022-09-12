import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertySingleComponent } from './property-single.component';

describe('PropertySingleComponent', () => {
  let component: PropertySingleComponent;
  let fixture: ComponentFixture<PropertySingleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertySingleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertySingleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
