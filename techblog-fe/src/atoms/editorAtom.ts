import { atom } from 'recoil';

import {Crepe} from "@/editor";

export const editorAtom = atom<Crepe | null>({
    key: 'editorAtom',
    default: null,
});