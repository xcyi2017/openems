import { Component, Input } from '@angular/core';
import { Edge, EdgeConfig, Service, ChannelAddress } from '../../../shared/shared';
import { DefaultTypes } from 'src/app/shared/service/defaulttypes';
import { Cumulated } from 'src/app/shared/jsonrpc/response/queryHistoricTimeseriesEnergyResponse';
import { ActivatedRoute } from '@angular/router';
import { ModalController } from '@ionic/angular';
import { GridModalComponent } from './modal/modal.component';

@Component({
    selector: GridComponent.SELECTOR,
    templateUrl: './widget.component.html'
})
export class GridComponent {

    @Input() public period: DefaultTypes.HistoryPeriod;

    private static readonly SELECTOR = "gridWidget";

    public data: Cumulated = null;
    public values: any;
    public edge: Edge = null;

    constructor(
        public service: Service,
        private route: ActivatedRoute,
        public modalCtrl: ModalController,
    ) { }

    ngOnInit() {
        this.service.setCurrentComponent('', this.route).then(response => {
            this.edge = response;
        });
    }

    ngOnChanges() {
        this.updateValues();
    };

    updateValues() {
        let channels: ChannelAddress[] = [
            new ChannelAddress('_sum', 'GridBuyActiveEnergy'),
            new ChannelAddress('_sum', 'GridSellActiveEnergy'),
        ];

        this.service.queryEnergy(this.period.from, this.period.to, channels).then(response => {
            this.data = response.result.data;
        }).catch(reason => {
            console.error(reason); // TODO error message
        });
    };

    async presentModal() {
        const modal = await this.modalCtrl.create({
            component: GridModalComponent,
        });
        return await modal.present();
    }
}