import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPropertyFormComponent } from './add-property-form.component';

describe('AddPropertyFormComponent', () => {
  let component: AddPropertyFormComponent;
  let fixture: ComponentFixture<AddPropertyFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPropertyFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPropertyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
