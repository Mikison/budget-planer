import {Component, ViewChild} from '@angular/core';
import {RouterLink} from "@angular/router";
import {
  BrnPopoverComponent,
  BrnPopoverContentDirective,
  BrnPopoverTriggerDirective
} from "@spartan-ng/ui-popover-brain";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmCommandImports} from "@spartan-ng/ui-command-helm";
import {HlmIconComponent, provideIcons} from "@spartan-ng/ui-icon-helm";
import {HlmPopoverContentDirective} from "@spartan-ng/ui-popover-helm";
import {
  HlmCardContentDirective,
  HlmCardDescriptionDirective,
  HlmCardDirective,
  HlmCardFooterDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";
import {HlmLabelDirective} from "@spartan-ng/ui-label-helm";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmSkeletonComponent} from "@spartan-ng/ui-skeleton-helm";
import {lucideChevronRight, lucideEdit, lucideFolderPlus, lucideTrash2} from "@ng-icons/lucide";
import {BrnProgressComponent, BrnProgressIndicatorComponent} from "@spartan-ng/ui-progress-brain";
import {HlmProgressIndicatorDirective} from "@spartan-ng/ui-progress-helm";
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogTitleDirective
} from "@spartan-ng/ui-dialog-helm";
import {BrnDialogContentDirective, BrnDialogTriggerDirective} from "@spartan-ng/ui-dialog-brain";
import {AddDialogComponent} from "./add-category-dialog/add-dialog.component";
import {DateCarouselComponent} from "./date-carousel/date-carousel.component";
import {
  HlmMenuComponent,
  HlmMenuGroupComponent,
  HlmMenuItemCheckboxDirective,
  HlmMenuItemCheckComponent,
  HlmMenuItemDirective,
  HlmMenuItemIconDirective,
  HlmMenuItemRadioComponent,
  HlmMenuItemRadioDirective,
  HlmMenuItemSubIndicatorComponent,
  HlmMenuLabelComponent,
  HlmMenuSeparatorComponent,
  HlmMenuShortcutComponent,
  HlmSubMenuComponent
} from "@spartan-ng/ui-menu-helm";
import {BrnContextMenuTriggerDirective, BrnMenuTriggerDirective} from "@spartan-ng/ui-menu-brain";
import {
  BrnAlertDialogComponent,
  BrnAlertDialogContentDirective,
  BrnAlertDialogTriggerDirective
} from "@spartan-ng/ui-alertdialog-brain";
import {
  HlmAlertDialogActionButtonDirective,
  HlmAlertDialogCancelButtonDirective,
  HlmAlertDialogComponent,
  HlmAlertDialogContentComponent,
  HlmAlertDialogDescriptionDirective,
  HlmAlertDialogFooterComponent,
  HlmAlertDialogHeaderComponent,
  HlmAlertDialogOverlayDirective,
  HlmAlertDialogTitleDirective
} from "@spartan-ng/ui-alertdialog-helm";


@Component({
  selector: 'app-budget-category',
  standalone: true,
  imports: [
    RouterLink,
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
    HlmSkeletonComponent,
    BrnProgressComponent,
    BrnProgressIndicatorComponent,
    HlmProgressIndicatorDirective,
    HlmButtonDirective,
    BrnDialogTriggerDirective,
    BrnDialogContentDirective,

    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogHeaderComponent,
    HlmDialogFooterComponent,
    HlmDialogTitleDirective,
    HlmDialogDescriptionDirective,

    HlmLabelDirective,
    HlmInputDirective,
    HlmButtonDirective,
    AddDialogComponent,
    DateCarouselComponent,
    BrnMenuTriggerDirective,
    BrnContextMenuTriggerDirective,

    HlmMenuComponent,
    HlmSubMenuComponent,
    HlmMenuItemDirective,
    HlmMenuItemSubIndicatorComponent,
    HlmMenuLabelComponent,
    HlmMenuShortcutComponent,
    HlmMenuSeparatorComponent,
    HlmMenuItemIconDirective,
    HlmMenuItemCheckComponent,
    HlmMenuItemRadioComponent,
    HlmMenuGroupComponent,
    HlmMenuItemCheckboxDirective,
    HlmMenuItemRadioDirective,
    BrnAlertDialogTriggerDirective,
    BrnAlertDialogContentDirective,

    HlmAlertDialogComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogTitleDirective,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogContentComponent,
  ],
  providers: [provideIcons({lucideFolderPlus, lucideChevronRight, lucideEdit, lucideTrash2})],
  templateUrl: './budget-category.component.html',
  styles: [`
    .hover-parent {
      position: relative;
    }

    .hover-child {
      transition: opacity 0.3s ease-in-out;
    }

    .hover-parent:hover .hover-child {
      opacity: 1;
    }
  `]
})
export class BudgetCategoryComponent {


  value = 23;


}
