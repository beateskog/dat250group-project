import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrivatePollMessageComponent } from './private-poll-message.component';

describe('PrivatePollMessageComponent', () => {
  let component: PrivatePollMessageComponent;
  let fixture: ComponentFixture<PrivatePollMessageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrivatePollMessageComponent]
    });
    fixture = TestBed.createComponent(PrivatePollMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
