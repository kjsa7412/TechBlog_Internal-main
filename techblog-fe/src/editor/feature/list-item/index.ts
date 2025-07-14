// import { listItemBlockConfig } from '@milkdown/kit/component/list-item-block'
import { html } from 'atomico'
import clsx from 'clsx'

// 한글 작성 이슈 때문에 기존 라이브러리가 아닌 수정한 라이브러리 import
import { listItemBlockComponent, listItemBlockConfig } from "@/editor/list-item-block";
import type { Ctx } from '@milkdown/kit/ctx'
import type { DefineFeature, Icon } from '../shared'

import { bulletIcon, checkBoxCheckedIcon, checkBoxUncheckedIcon } from '../../icons'

export interface ListItemConfig {
  bulletIcon: Icon
  checkBoxCheckedIcon: Icon
  checkBoxUncheckedIcon: Icon
}

export type ListItemFeatureConfig = Partial<ListItemConfig>

function configureListItem(ctx: Ctx, config?: ListItemFeatureConfig) {
  ctx.set(listItemBlockConfig.key, {
    renderLabel: ({ label, listType, checked, readonly }) => {
      if (checked == null) {
        if (listType === 'bullet')
          return html`<span class='label'>${config?.bulletIcon?.() ?? bulletIcon}</span>`

        return html`<span class='label'>${label}</span>`
      }

      if (checked)
        return html`<span class=${clsx('label checkbox', readonly && 'readonly')}>${config?.checkBoxCheckedIcon?.() ?? checkBoxCheckedIcon}</span>`

      return html`<span class=${clsx('label checkbox', readonly && 'readonly')}>${config?.checkBoxUncheckedIcon?.() ?? checkBoxUncheckedIcon}</span>`
    },
  })
}

export const defineFeature: DefineFeature<ListItemFeatureConfig> = (editor, config) => {
  editor
    .config(ctx => configureListItem(ctx, config))
    .use(listItemBlockComponent)
}
