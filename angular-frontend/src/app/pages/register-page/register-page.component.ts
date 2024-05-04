import {Component, inject} from '@angular/core';
import {HlmButtonDirective} from '@spartan-ng/ui-button-helm';
import {
  HlmCardContentDirective,
  HlmCardDescriptionDirective,
  HlmCardDirective,
  HlmCardFooterDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective,
} from '@spartan-ng/ui-card-helm';
import {HlmCommandImports} from '@spartan-ng/ui-command-helm';
import {HlmIconComponent} from '@spartan-ng/ui-icon-helm';
import {HlmInputDirective} from '@spartan-ng/ui-input-helm';
import {HlmLabelDirective} from '@spartan-ng/ui-label-helm';
import {HlmPopoverContentDirective} from '@spartan-ng/ui-popover-helm';
import {
  BrnPopoverComponent,
  BrnPopoverContentDirective,
  BrnPopoverTriggerDirective,
} from '@spartan-ng/ui-popover-brain';
import {RouterModule} from "@angular/router";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {enviroment} from "../../enviroments/enviroments";
import {UserInterface} from "../../model/user.interface";

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    HlmCommandImports,
    HlmIconComponent,
    BrnPopoverComponent,
    BrnPopoverTriggerDirective,
    BrnPopoverContentDirective,
    HlmPopoverContentDirective,
    HlmCardDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective,
    HlmCardDescriptionDirective,
    HlmCardContentDirective,
    HlmLabelDirective,
    HlmInputDirective,
    HlmCardFooterDirective,
    HlmButtonDirective,
    ReactiveFormsModule,
    RouterModule],
  templateUrl: './register-page.component.html',
})
export class RegisterPageComponent {
  fb = inject(FormBuilder);
  http = inject(HttpClient);


  private API_URL = enviroment.api;

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    email: ['', Validators.required],
    username: ['', Validators.required],
    password: ['', Validators.required],
  })


  onSubmit() {
    this.http.post<{user : UserInterface}>(this.API_URL + "/auth/register",
      this.form.getRawValue()
    ).subscribe((response) => {
      console.log('response',response);
    })
  }


}
