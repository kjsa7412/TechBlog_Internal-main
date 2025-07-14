import {atom} from "recoil";

export interface IPostAtom {
    slug: string,
    isMe: boolean;
}

export const postAtom = atom<IPostAtom>({
    key: 'postAtom',
    default: {
        slug: "",
        isMe: false
    }
});