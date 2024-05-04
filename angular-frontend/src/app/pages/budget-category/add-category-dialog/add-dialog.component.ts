import { Component } from '@angular/core';
import {BrnDialogContentDirective, BrnDialogTriggerDirective} from "@spartan-ng/ui-dialog-brain";
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective, HlmDialogFooterComponent,
  HlmDialogHeaderComponent, HlmDialogTitleDirective
} from "@spartan-ng/ui-dialog-helm";
import {HlmLabelDirective} from "@spartan-ng/ui-label-helm";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmIconComponent, provideIcons} from "@spartan-ng/ui-icon-helm";
import {lucideFolderPlus} from "@ng-icons/lucide";

@Component({
  selector: 'app-add-category-dialog',
  standalone: true,
  imports: [BrnDialogTriggerDirective,
    BrnDialogContentDirective,

    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogHeaderComponent,
    HlmDialogFooterComponent,
    HlmDialogTitleDirective,
    HlmDialogDescriptionDirective,

    HlmLabelDirective,
    HlmInputDirective,
    HlmButtonDirective, HlmIconComponent, HlmIconComponent,],
  templateUrl: './add-dialog.component.html',
  providers: [provideIcons({lucideFolderPlus})]
})
export class AddDialogComponent {

}
