import { Button } from "flowbite-react";
import { useTranslation } from "react-i18next";
import bgRestaurant from "../../assets/img/bg_restaurant.png";

export default function HeroSection() {
  const { t } = useTranslation();

  return (
    <section
      id="hero"
      className="relative h-screen bg-cover bg-center"
      style={{ backgroundImage: `url(${bgRestaurant})` }}>
      {/* Overlay tối + blur */}
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm"></div>

      {/* Nội dung */}
      <div className="relative z-10 flex flex-col items-center justify-center h-full text-center px-4">
        <h1 className="text-5xl font-extrabold text-white mb-6">
          {t("heroSection.title.prefix")}{" "}
          <span className="bg-gradient-to-r from-yellow-400 to-orange-500 bg-clip-text text-transparent">
            {t("heroSection.title.highlight")}
          </span>
        </h1>
        <h2 className="text-lg text-gray-200 max-w-2xl mb-8">
          {t("heroSection.subtitle")}
        </h2>

        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Button
            size="lg"
            href="/table"
            className="font-semibold bg-amber-900 text-amber-50 shadow-md hover:scale-105 transition-transform duration-200">
            {t("heroSection.button.bookTable")}
          </Button>
          <Button
            size="lg"
            href="/menu"
            className="font-semibold bg-amber-900 text-amber-50 shadow-md hover:scale-105 transition-transform duration-200">
            {t("heroSection.button.orderNow")}
          </Button>
        </div>
      </div>
    </section>
  );
}
