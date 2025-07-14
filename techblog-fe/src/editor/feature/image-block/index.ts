import { imageBlockComponent, imageBlockConfig } from '@milkdown/kit/component/image-block'
import { imageInlineComponent, inlineImageConfig } from '@milkdown/kit/component/image-inline'

import axiosServer from "@/libs/axiosServer";
import type { DefineFeature, Icon } from '../shared'
import {IMG} from "@/contants/common";

import { captionIcon, confirmIcon, imageIcon } from '../../icons'

interface ImageBlockConfig {
  onUpload: (file: File) => Promise<string>

  inlineImageIcon: Icon
  inlineConfirmButton: Icon
  inlineUploadButton: Icon
  inlineUploadPlaceholderText: string
  inlineOnUpload: (file: File) => Promise<string>

  blockImageIcon: Icon
  blockConfirmButton: Icon
  blockCaptionIcon: Icon
  blockUploadButton: Icon
  blockCaptionPlaceholderText: string
  blockUploadPlaceholderText: string
  blockOnUpload: (file: File) => Promise<string>
}

// 커스텀 이미지 저장 시작
const customUploadHandler = async (file: File): Promise<string> => {
  const maxSizeInMB = 5;
  const allowedExtensions = ['png', 'jpg', 'gif'];

  // 파일 크기 체크 (MB 단위로 변환)
  const fileSizeInMB = file.size / (1024 * 1024);
  if (fileSizeInMB > maxSizeInMB) {
    return Promise.reject('File size must be 5MB or less.');
  }

  // 파일 확장자 체크
  const fileExtension = file.name.split('.').pop()?.toLowerCase();
  if (!fileExtension || !allowedExtensions.includes(fileExtension)) {
    return Promise.reject('Only PNG, JPG, and GIF formats are allowed.');
  }

  const formData = new FormData();
  formData.append('file', file);

  const result = await axiosServer.post('/private/img/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });

  if (result.status === 200 && result.data.content && !result.data.isError) {
    return IMG.DefaultPath + result.data.content.imgfullpath;
  } else {
    alert('이미지 업로드를 실패하였습니다.');
    return Promise.reject('Upload failed');
  }
};

export type ImageBlockFeatureConfig = Partial<ImageBlockConfig>

export const defineFeature: DefineFeature<ImageBlockFeatureConfig> = (editor, config) => {
  editor
    .config((ctx) => {
      ctx.update(inlineImageConfig.key, value => ({
        uploadButton: config?.inlineUploadButton ?? (() => 'Upload'),
        imageIcon: config?.inlineImageIcon ?? (() => imageIcon),
        confirmButton: config?.inlineConfirmButton ?? (() => confirmIcon),
        uploadPlaceholderText: config?.inlineUploadPlaceholderText ?? 'or paste link (Allows JPG, PNG, or GIF files, 5MB max)',
        onUpload: config?.inlineOnUpload ?? customUploadHandler
      }))
      ctx.update(imageBlockConfig.key, value => ({
        uploadButton: config?.blockUploadButton ?? (() => 'Upload file'),
        imageIcon: config?.blockImageIcon ?? (() => imageIcon),
        captionIcon: config?.blockCaptionIcon ?? (() => captionIcon),
        confirmButton: config?.blockConfirmButton ?? (() => 'Confirm'),
        captionPlaceholderText: config?.blockCaptionPlaceholderText ?? 'Write Image Caption',
        uploadPlaceholderText: config?.blockUploadPlaceholderText ?? 'or paste link (Allows JPG, PNG, or GIF files, 5MB max)',
        onUpload: config?.blockOnUpload ?? customUploadHandler
      }))
    })
    .use(imageBlockComponent)
    .use(imageInlineComponent)
}
