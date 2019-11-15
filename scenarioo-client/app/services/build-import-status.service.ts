import { Injectable } from '@angular/core';
import {IBuildImportStatus} from '../generated-types/backend-types';

@Injectable()
export class BuildImportStatusService {
    static readonly STYLE_CLASSES_FOR_BUILD_IMPORT_STATUS: Map<IBuildImportStatus, string> = new Map<IBuildImportStatus, string>([
        ['SUCCESS', 'label-success'],
        ['FAILED', 'label-danger'],
        ['UNPROCESSED', 'label-default'],
        ['QUEUED_FOR_PROCESSING', 'label-info'],
        ['PROCESSING', 'label-primary'],
        ['OUTDATED', 'label-warning'],
    ]);

    getStyleClassForBuildImportStatus(status: IBuildImportStatus): string {
        const styleClassFromMapping = BuildImportStatusService.STYLE_CLASSES_FOR_BUILD_IMPORT_STATUS.get(status);
        return styleClassFromMapping ? styleClassFromMapping : 'label-warning';
    }
}
