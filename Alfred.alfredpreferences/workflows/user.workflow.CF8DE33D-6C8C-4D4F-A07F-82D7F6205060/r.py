import urllib2
from xml.etree import ElementTree as ET


def gen_alfred_subitem(parent_node, index, title, desc, link):
    title_str = '%s: %s' % (index, title)
    item_node = ET.SubElement(parent_node, 'item', {'uid': link, 'arg': link, 'valid': 'YES'})

    title_node = ET.SubElement(item_node, 'title')
    title_node.text = title_str
    subtitle_node = ET.SubElement(item_node, 'subtitle')
    subtitle_node.text = desc
    icon_node = ET.SubElement(item_node, 'icon')
    icon_node.text = 'icon.png'
    return item_node


def query_reddit(reddit=''):
    if reddit:
        rss_url = 'http://www.reddit.com/r/%s.rss' % reddit
    else:
        rss_url = 'http://www.reddit.com/.rss'
    alfred_root = ET.Element('items')

    req = urllib2.Request(url=rss_url, headers={'User-Agent': 'alfred-workflow-r/1.0'})
    try:
        body = urllib2.urlopen(req).read()
    except urllib2.HTTPError, e:
        gen_alfred_subitem(alfred_root, 1, str(e.code) , '>_<', '')
    else:
        reddit_root = ET.fromstring(body)
        items = reddit_root.findall('channel/item')
        for index, item_node in enumerate(items[:42]):
            title = item_node.findtext('title')
            pub_date = item_node.findtext('pubDate')
            reddit_link = item_node.findtext('guid')

            gen_alfred_subitem(alfred_root, index+1, title, pub_date, reddit_link)
    return ET.tostring(alfred_root, 'utf8', 'xml')
