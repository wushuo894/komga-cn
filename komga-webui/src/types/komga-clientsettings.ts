export interface ClientSettingDto {
  value: string,
  allowUnauthorized?: boolean,
}

export interface ClientSettingGlobalUpdateDto {
  value: string,
  allowUnauthorized: boolean,
}

export interface ClientSettingUserUpdateDto {
  value: string,
}

export enum CLIENT_SETTING {
  WEBUI_OAUTH2_HIDE_LOGIN = 'webui.oauth2.hide_login',
  WEBUI_OAUTH2_AUTO_LOGIN = 'webui.oauth2.auto_login',
  WEBUI_POSTER_STRETCH = 'webui.poster.stretch',
  WEBUI_POSTER_BLUR_UNREAD = 'webui.poster.blur_unread',
  WEBUI_LIBRARIES = 'webui.libraries',
  WEBUI_SERIES_GROUPS = 'webui.series_groups',
}

export interface ClientSettingLibrary {
  unpinned?: boolean,
  order?: number,
}

export interface ClientSettingLibraryUpdate {
  libraryId: string,
  patch: ClientSettingLibrary,
}

export interface ClientSettingsSeriesGroup {
  name: string,
  groups: Record<string, string[]>
}

export const SERIES_GROUP_ALPHA = {
  name: 'alpha',
  groups: {
    'A': ['A'],
    'B': ['B'],
    'C': ['C'],
    'D': ['D'],
    'E': ['E'],
    'F': ['F'],
    'G': ['G'],
    'H': ['H'],
    'I': ['I'],
    'J': ['J'],
    'K': ['K'],
    'L': ['L'],
    'M': ['M'],
    'N': ['N'],
    'O': ['O'],
    'P': ['P'],
    'Q': ['Q'],
    'R': ['R'],
    'S': ['S'],
    'T': ['T'],
    'U': ['U'],
    'V': ['V'],
    'W': ['W'],
    'X': ['X'],
    'Y': ['Y'],
    'Z': ['Z'],
  },
} as ClientSettingsSeriesGroup

export const SERIES_GROUP_JAPANESE = {
  name: 'japanese',
  groups: {
    'あ': ['あ', 'ア'],
    'い': ['い', 'イ'],
    'う': ['う', 'ゔ', 'ウ', 'ヴ'],
    'え': ['え', 'エ'],
    'お': ['お', 'オ'],
    'か': ['か', 'が', 'カ', 'ガ'],
    'き': ['き', 'ぎ', 'キ', 'ギ'],
    'く': ['く', 'ぐ', 'ク', 'グ'],
    'け': ['け', 'げ', 'ケ', 'ゲ'],
    'こ': ['こ', 'ご', 'コ', 'ゴ'],
    'さ': ['さ', 'ざ', 'サ', 'ザ'],
    'し': ['し', 'じ', 'シ', 'ジ'],
    'す': ['す', 'ず', 'ス', 'ズ'],
    'せ': ['せ', 'ぜ', 'セ', 'ゼ'],
    'そ': ['そ', 'ぞ', 'ソ', 'ゾ'],
    'た': ['た', 'だ', 'タ', 'ダ'],
    'ち': ['ち', 'ぢ', 'チ', 'ヂ'],
    'つ': ['つ', 'づ', 'ツ', 'ズ'],
    'て': ['て', 'で', 'テ', 'デ'],
    'と': ['と', 'ど', 'ト', 'ド'],
    'は': ['は', 'ば', 'ぱ', 'ハ', 'バ', 'パ'],
    'ひ': ['ひ', 'び', 'ぴ', 'ヒ', 'ビ', 'ピ'],
    'ふ': ['ふ', 'ぶ', 'ぷ', 'フ', 'ブ', 'プ'],
    'へ': ['へ', 'べ', 'ぺ', 'ヘ', 'ベ', 'ベ'],
    'ほ': ['ほ', 'ぼ', 'ぽ', 'ホ', 'ボ', 'ポ'],
    'ま': ['ま', 'マ'],
    'み': ['み', 'ミ'],
    'む': ['む', 'ム'],
    'め': ['め', 'メ'],
    'も': ['も', 'モ'],
    'や': ['や', 'ヤ'],
    'ゆ': ['ゆ', 'ユ'],
    'よ': ['よ', 'ヨ'],
    'ら': ['ら', 'ラ'],
    'り': ['り', 'リ'],
    'る': ['る', 'ル'],
    'れ': ['れ', 'レ'],
    'ろ': ['ろ', 'ロ'],
    'わ': ['わ', 'ワ'],
  },
} as ClientSettingsSeriesGroup
