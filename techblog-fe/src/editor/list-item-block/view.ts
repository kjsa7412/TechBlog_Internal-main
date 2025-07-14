import { $view } from '@milkdown/utils'
import { listItemSchema } from '@milkdown/preset-commonmark'
import { TextSelection } from '@milkdown/prose/state'

import type { NodeViewConstructor } from '@milkdown/prose/view'
import type { Node } from '@milkdown/prose/model'
import { listItemBlockConfig } from './config'
import type { ListItemComponentProps } from './component'

import { withMeta } from '../__internal__/meta'
import { defIfNotExists } from '../__internal__/helper'
import { ListItemElement } from './component'

defIfNotExists('milkdown-list-item-block', ListItemElement)
export const listItemBlockView = $view(listItemSchema.node, (ctx): NodeViewConstructor => {
  return (initialNode, view, getPos) => {
    const dom = document.createElement('milkdown-list-item-block') as HTMLElement & ListItemComponentProps
    const contentDOM = document.createElement('div')
    contentDOM.setAttribute('data-content-dom', 'true')
    contentDOM.classList.add('content-dom')
    const config = ctx.get(listItemBlockConfig.key)
    const bindAttrs = (node: Node) => {
      dom.listType = node.attrs.listType
      dom.label = node.attrs.label
      dom.checked = node.attrs.checked

      dom.readonly = !view.editable
    }

    bindAttrs(initialNode)
    dom.appendChild(contentDOM)
    dom.selected = false
    dom.setAttr = (attr, value) => {
      const pos = getPos()
      if (pos == null)
        return

      view.dispatch(view.state.tr.setNodeAttribute(pos, attr, value))
    }
    dom.onMount = () => {
      const pos = getPos() ?? 0
      const end = pos + initialNode.nodeSize
      const { from, to } = view.state.selection
      if (view.hasFocus() && pos < from && to < end) {
        Promise.resolve().then(() => {
          const p = view.state.doc.resolve(pos)
          view.dispatch(view.state.tr.setSelection(TextSelection.near(p, 1)))
        })
      }
    }
    let node = initialNode
    dom.config = config
    return {
      dom,
      contentDOM,
      update: (updatedNode) => {
        if (updatedNode.type !== initialNode.type)
          return false

        // 기존에는 return false로 되어 있는데 이걸 사용하면 lits-item에 한글 입력 시 오류가 생김
        // 따라서 return true로 변경
        if (updatedNode.sameMarkup(node) && updatedNode.content.eq(node.content)) {
          // return false;
          return true;
        }

        node = updatedNode
        bindAttrs(updatedNode)
        return true
      },
      ignoreMutation: (mutation) => {
        if (!dom || !contentDOM)
          return true

        if ((mutation.type as unknown) === 'selection')
          return false

        if (contentDOM === mutation.target && mutation.type === 'attributes')
          return true

        if (contentDOM.contains(mutation.target))
          return false

        return true
      },
      selectNode: () => {
        dom.selected = true
      },
      deselectNode: () => {
        dom.selected = false
      },
      destroy: () => {
        dom.remove()
        contentDOM.remove()
      },
    }
  }
})

withMeta(listItemBlockView, {
  displayName: 'NodeView<list-item-block>',
  group: 'ListItemBlock',
})
