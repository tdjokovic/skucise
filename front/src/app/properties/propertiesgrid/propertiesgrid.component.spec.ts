import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertiesgridComponent } from './propertiesgrid.component';

describe('PropertiesgridComponent', () => {
  let component: PropertiesgridComponent;
  let fixture: ComponentFixture<PropertiesgridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertiesgridComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertiesgridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
