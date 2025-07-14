import { atom } from 'recoil';

export const editTitleAtom = atom<string>({
    key: 'editTitleAtom',
    default: ''
});