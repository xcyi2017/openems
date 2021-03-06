import { Component, Input } from '@angular/core';
import { Edge, Service, Websocket, EdgeConfig } from '../../../../shared/shared';
import { ModalController } from '@ionic/angular';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: ConsumptionModalComponent.SELECTOR,
    templateUrl: './modal.component.html'
})
export class ConsumptionModalComponent {

    private static readonly SELECTOR = "consumption-modal";

    @Input() public edge: Edge;
    @Input() public evcsComponents: EdgeConfig.Component[];
    @Input() public currentTotalChargingPower: () => number;

    public config: EdgeConfig = null;

    constructor(
        public service: Service,
        public modalCtrl: ModalController
    ) { }

    ngOnInit() {
    }
}