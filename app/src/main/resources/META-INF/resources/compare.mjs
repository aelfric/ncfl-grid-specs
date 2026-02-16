import HtmlDiff from './htmldiff.min.mjs'

export function doCompare() {
    const oldHtml = document.getElementById('before');
    const newHtml = document.getElementById('after');
    const diffHtml = document.getElementById('result-container');

    diffHtml.innerHTML = HtmlDiff.execute(oldHtml.innerHTML, newHtml.innerHTML);
}

globalThis.doCompare = doCompare